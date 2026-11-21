window.inactivityManager = {
    timer: null,
    dotnetReference: null,
    timeoutMilliseconds: 10 * 60 * 1000,
    lastActivity: Date.now(),

    start: function (dotnetReference) {
        this.dotnetReference = dotnetReference;
        this.lastActivity = Date.now();

        const events = [
            "mousedown",
            "mousemove",
            "keydown",
            "scroll",
            "touchstart",
            "click"
        ];

        this.activityHandler = () => {
            this.lastActivity = Date.now();
            this.resetTimer();
        };

        events.forEach(eventName => {
            document.addEventListener(
                eventName,
                this.activityHandler,
                true
            );
        });

        document.addEventListener(
            "visibilitychange",
            this.visibilityHandler = () => {
                if (!document.hidden) {
                    this.checkInactivity();
                }
            }
        );

        this.resetTimer();
    },

    resetTimer: function () {
        if (this.timer) {
            clearTimeout(this.timer);
        }

        this.timer = setTimeout(
            () => this.checkInactivity(),
            this.timeoutMilliseconds
        );
    },

    checkInactivity: function () {
        const elapsed =
            Date.now() - this.lastActivity;

        if (elapsed >= this.timeoutMilliseconds) {
            if (this.dotnetReference) {
                this.dotnetReference.invokeMethodAsync(
                    "CerrarPorInactividad"
                );
            }

            return;
        }

        this.timer = setTimeout(
            () => this.checkInactivity(),
            this.timeoutMilliseconds - elapsed
        );
    },

    stop: function () {
        if (this.timer) {
            clearTimeout(this.timer);
        }

        const events = [
            "mousedown",
            "mousemove",
            "keydown",
            "scroll",
            "touchstart",
            "click"
        ];

        if (this.activityHandler) {
            events.forEach(eventName => {
                document.removeEventListener(
                    eventName,
                    this.activityHandler,
                    true
                );
            });
        }

        if (this.visibilityHandler) {
            document.removeEventListener(
                "visibilitychange",
                this.visibilityHandler
            );
        }

        this.dotnetReference = null;
    }
};