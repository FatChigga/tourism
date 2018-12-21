package com.iptv.core.shiro;

import com.iptv.core.common.UserStatusEnum;
import com.iptv.core.utils.JsonUtil;
import com.iptv.sys.service.SysRoleMenuService;
import com.iptv.sys.service.SysUserRoleService;
import com.iptv.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述: shiro鉴权类
 **/
public class MyShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /*@Autowired
    private SessionDAO sessionDAO;*/

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("调用授权检测。。。。。。");
        //获取登录用户名
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        if (StringUtils.isNotEmpty(shiroUser.getId())) {
            try {
                Map permisionParam = new HashMap();
                permisionParam.put("id",shiroUser.getId());
                List<Map> roleList = sysUserRoleService.roleListByUserId(shiroUser.getId());
                /*Set<String> roles = new LinkedHashSet<>();*/
                Set<String> resources = new LinkedHashSet<>();
                //BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps("RoleResources");
                BoundHashOperations<String, String, String> ops = null;
                for (int i = 0; i < roleList.size(); i++) {
                    /*roles.add(roleList.get(i).get("role_name").toString());*/
                    String key = roleList.get(i).get("RoleId").toString();
                    if (ops.hasKey(key)) {
                        List roleResourceArray = (List) JsonUtil.getObject(ops.get(key));
                        for (int j = 0; j < roleResourceArray.size(); j++) {
                            Map<String, Object> mapList = (Map<String, Object>) roleResourceArray.get(j);
                            /*if (mapList.get("shiro_mark")!=null){*/
                            resources.add(mapList.get("shiro_mark").toString());
                            /*}*/
                        }
                    }
                }
                //添加角色和权限
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                /*simpleAuthorizationInfo.setRoles(roles);*/
                simpleAuthorizationInfo.setStringPermissions(resources);
                /*logger.info(String.format("用户[%s]登录成功,获取权限集合:%s",roles, resources.toString()));*/
                return simpleAuthorizationInfo;
            } catch (Exception e) {
                throw new AuthenticationException();
            }
        } else {
            throw new UnknownAccountException();
        }
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        /*logger.info(String.format("用户[%s]尝试登录,...", token.getUsername()));*/
        //获取用户信息
        String account = token.getUsername();
        char[] password = token.getPassword();
        if (StringUtils.isEmpty(account)) {
            throw new UnknownAccountException();
        }
        if (password == null) {
            throw new UnknownAccountException();
        }

        Map user = sysUserService.getUserByAccount(account);

        if (user == null) {
            throw new UnknownAccountException();
        } else {
            if (UserStatusEnum.DISABLE.getNodeCode().equals(user.get("Status"))) {
                throw new DisabledAccountException();
            }
        }
        //查询用户的角色
        try {
            List<Map> roleList = sysUserRoleService.roleListByUserId(user.get("Id").toString());
            List<Map<String, Object>> listResource = new ArrayList<>();
            for (int i = 0; i < roleList.size(); i++) {
                List permissionList = sysRoleMenuService.permissionList(roleList.get(i));
                for (int j = 0; j < permissionList.size(); j++) {
                    Map<String, Object> permission = (Map<String, Object>) permissionList.get(j);
                    listResource.add(permission);
                }
            }
            //如果用户处于未激活状态，登陆认证以后修改为激活状态
            if (UserStatusEnum.NOTACTIVE.getNodeCode().equals(user.get("status"))) {
                Map userParam = new HashMap();
                userParam.put("Status",UserStatusEnum.ACTIVATE.getNodeCode());
                userParam.put("UpdateTime",new Date());
                userParam.put("ActivateTime",new Date());
                userParam.put("Id",user.get("Id"));
                sysUserService.loginUpdateUser(userParam);
                /*operationLogService.addOperationLoginLog(user.getAccount(), user.getId(),
                        OperationTypeEnum.CREATE.getNodeCode(),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(),
                        operationParams, "用户登录修改为激活状态");*/
            }
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            listResource = removeDuplicateResource(listResource);
            Stream<Map<String, Object>> stream = listResource.stream();
            listResource = stream.sorted(MyShiroRealm::comparator).collect(Collectors.toList());
            session.setAttribute("menus", listResource);// 用户拥有的菜单
        } catch (Exception e) {
            throw new AuthenticationException();
        }
        //设置盐值
        ByteSource salt = ByteSource.Util.bytes(user.get("LoginName"));
        ShiroUser shiroUser = new ShiroUser(user.get("Id").toString(),
                user.get("LoginName").toString(), user.get("UserName").toString());
        return new SimpleAuthenticationInfo(shiroUser, user.get("Password"), salt, getName());
    }

    public static class ShiroUser implements Serializable {
        private String id;
        private String account;
        private String realName;

        public ShiroUser() {

        }

        public ShiroUser(String id, String account, String realName) {
            this.id = id;
            this.account = account;
            this.realName = realName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }


    private static ArrayList<Map<String, Object>> removeDuplicateResource(List<Map<String, Object>> resourceEntities) {
        Set<Map<String, Object>> set = new TreeSet<>(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                //字符串,则按照asicc码升序排列
                return o1.get("Id").toString().compareTo(o2.get("Id").toString());
            }
        });
        set.addAll(resourceEntities);
        return new ArrayList<>(set);
    }

    private static int comparator(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1 == null && map2 == null)
            return 0;
        if (map1 == null || map2 == null) {
            throw new NullPointerException();
        }
        int sort1 = (int) map1.get("OrderNum");
        int sort2 = (int) map2.get("OrderNum");

        return  sort1- sort2;
    }


    /*public boolean deleteSession(String id){
        //以下为只允许同一账户单个登录
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        if(sessions.size()>0) {
            for (Session session : sessions) {
                //获得session中已经登录用户的名字
                if(null!=session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY")) {
                    SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection)session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY");
                    Collection<ShiroUser> shiroUser = simplePrincipalCollection.byType(ShiroUser.class);
                    ShiroUser shiroUser1 = shiroUser.iterator().next();
                    if (id.equals(shiroUser1.getAccount())) {  //这里的username也就是当前登录的username
                        session.setTimeout(0);  //这里就把session清除，
                    }
                }
            }
        }
        return true;
    }*/
}