(function ($) {
    function Slider(elem, options) {
        this.$container = elem;
        this.default = {
            width: this.$container.width() - 2,
            height: this.$container.outerHeight() - 2,
            bgColor: '#E8E8E8',
            progressColor: '#FFE97F',
            handleColor: '#fff',
            succColor: '#78D02E',
            text: '请按住滑块，拖动到最右边',//未解锁的文字
            succText: '验证通过',//解锁成功的文字'
            textColor: '#666',
            succTextColor: '#fff',
            onSuccess: function () {
            }
        };
        this.options = $.extend({}, this.default, options);
        this.isSuccess = false;
    }

    Slider.prototype = {
        create: function () {
            var $container = this.$container;
            var options = this.options;
            initDOM();
            initStyle();

            function initDOM() {
                var template = '<div class="slide-to-unlock-bg"><span>' +
                    options.text +
                    '</span></div><div class="slide-to-unlock-progress"></div><div class="slide-to-unlock-handle handle-bg"></div>';
                $container.html(template);
            }

            function initStyle() {
                $container.css({
                    position: 'relative',
                });
                $container.find('span').css({
                    lineHeight: options.height + 'px',
                    fontSize: '13px',
                    color: options.textColor
                });
                $container.find('.slide-to-unlock-bg').css({
                    width: options.width + 'px',
                    height: options.height + 'px',
                    backgroundColor: options.bgColor,
                });
                $container.find('.slide-to-unlock-progress').css({
                    backgroundColor: options.progressColor,
                    height: options.height - 0 + 'px'
                });
                $container.find('.slide-to-unlock-handle').css({
                    backgroundColor: options.handleColor,
                    height: (options.height - 0) + 'px',
                    lineHeight: (options.height - 0) + 'px',
                    width: (Math.floor(options.width / 6.7)) + 'px',
                });
            }
        },
        bindDragEvent: function () {
            var that = this;
            var $container = this.$container;
            var options = this.options;
            var downX;
            var $prog = $container.find('.slide-to-unlock-progress'),
                $bg = $container.find('.slide-to-unlock-bg'),
                $handle = $container.find('.slide-to-unlock-handle');
            var succMoveWidth = $bg.width() - $handle.width();

            $handle.on('mousedown', null, mousedownHandler);
            $handle.on('touchstart', touchstartHandler);

            function getLimitNumber(num, min, max) {
                if (num > max) {
                    num = max;
                } else if (num < min) {
                    num = min;
                }
                return num;
            }

            function mousedownHandler(event) {
                downX = event.clientX;
                $(document).on('mousemove', null, mousemoveHandler);
                $(document).on('mouseup', null, mouseupHandler);
            }

            function mousemoveHandler(event) {
                var moveX = event.clientX;
                var diffX = getLimitNumber(moveX - downX, 0, succMoveWidth);
                $prog.width(diffX);
                $handle.css({
                    left: diffX
                });
                if (diffX === succMoveWidth) {
                    success();
                }
                event.preventDefault();
            }

            function mouseupHandler(event) {
                if (!that.isSuccess) {
                    $prog.animate({
                        width: 0
                    }, 100);
                    $handle.animate({
                        left: 0
                    }, 100);
                }
                $(document).off('mousemove', null, mousemoveHandler);
                $(document).off('mouseup', null, mouseupHandler);
            }

            function touchstartHandler(event) {
                var touch = event.originalEvent.targetTouches[0];
                downX = touch.pageX;
                $(document).on('touchmove', touchmoveHandler);
                $(document).on('touchup', touchupHandler);
            }

            function touchmoveHandler(event) {
                var touch = event.originalEvent.targetTouches[0];
                var moveX = touch.pageX;
                var diffX = getLimitNumber(moveX - downX, 0, succMoveWidth);
                $prog.width(diffX);
                $handle.css({
                    left: diffX
                });
                if (diffX === succMoveWidth) {
                    success();
                }
                event.preventDefault();
            }

            function touchupHandler(event) {
                if (!that.isSuccess) {
                    $prog.animate({
                        width: 0
                    }, 100);
                    $handle.animate({
                        left: 0
                    }, 100);
                }
                $(document).off('touchmove', touchmoveHandler);
                $(document).off('touchup', touchupHandler);
            }

            function success() {
                $prog.css({
                    backgroundColor: options.succColor,
                });
                $handle.css({'border-right': 'none', 'border-top-left-radius': '0', 'border-bottom-left-radius': '0'});
                $prog.css({'border-top-right-radius': '0', 'border-bottom-right-radius': '0'});
                $container.find('span').css({
                    color: options.succTextColor,
                    'font-weight': 'bold',
                    background: 'none',
                    '-webkit-text-fill-color': 'snow'
                });
                $handle.removeClass('handle-bg').addClass('handle-ok-bg');
                that.isSuccess = true;
                $container.find('span').html(options.succText);

                $handle.off('mousedown', null, mousedownHandler);
                $(document).off('mousemove', null, mousemoveHandler);

                $handle.off('touchstart', touchstartHandler);
                $(document).off('touchmove', touchmoveHandler);

                setTimeout(function () {
                    options.onSuccess && options.onSuccess();
                }, 30);
            }
        }
    };

    $.fn.extend({
        slideToUnlock: function (options) {
            return this.each(function () {
                var slider = new Slider($(this), options);
                slider.create();
                slider.bindDragEvent();
            });
        }
    });
})(jQuery);