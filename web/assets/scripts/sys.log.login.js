iv = {
    init: function () {
        this.bind();
        this.getInfo();
    },
    bind: function () {
        $("#grid").kendoGrid({
            columns: [
                {
                    title: "序号",
                    width: "80px",
                    field: "Id",
                    encoded: false,
                    attributes: {'class': 'center'},
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    title: "用户编号",
                    width: "100px",
                    field: "UserCode",
                    encoded: false,
                    attributes: {'class': 'center'}
                },
                {
                    title: "用户名称",
                    width: "100px",
                    field: "UserName",
                    encoded: false,
                    attributes: {'class': 'left'}
                },
                {
                    title: "IP地址",
                    width: "100px",
                    field: "IPAddress",
                    encoded: false,
                    attributes: {'class': 'center'}
                },
                {
                    title: "登录日期",
                    width: "100px",
                    field: "LoginDate",
                    encoded: false,
                    attributes: {'class': 'center'},
                    format: '{0:yyyy-MM-dd HH:mm:ss}'
                },
                {
                    title: "备注",
                    width: "100px",
                    field: "Remark",
                    encoded: false,
                    attributes: {'class': 'center'}
                }],
            page: 1,
            filterable: true,
            selectable: "Multiple, Row",
            scrollable: true,
            resizable: true,
            pageable: {
                "refresh": true,
                "autoBind": false,
                "input": true,
                "buttonCount": 10
            },
            autoBind: false,
            dataSource: createDataSource(basePath + 'sys/logLogin/logLoginList', {
                LoginDate: {
                    type: "date", parse: function (value) {
                        return new Date(value);
                    }
                },
                VedioCount: {type: "number"},
                Province: {type: "number"}
            }),
            dataBound: function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
        });
    },
    getInfo: function () {
        var grid = $('#grid').data('kendoGrid');
        grid.dataSource.filter({});
    }
}