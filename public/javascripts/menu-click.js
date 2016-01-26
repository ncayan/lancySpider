
    var $currdd = $(".menu-box dd[class*='curr']");
    if ($currdd.length > 0) {
        var $this = $currdd.eq(0);
        ShowDL($this);
    }
	
	
    $(".menu-box dt").click(function () { ShowDL($(this)); });
    function Nav($this) {
        var $nav = $this.parents("li");
        var $navs = $(".head-nav li");
        $navs.removeClass("curr");
        $nav.addClass("curr");
        $(".menu-box").hide();
        $("#" + $nav.attr("val")).show();
        return false;
    }
    function ShowDL($this) {
        var $parent = $this.parents("dl");
        var $thisMenuBox = $this.parents(".menu-box");
        var $navs = $(".head-nav li");
        var $dls = $thisMenuBox.find("dl");

        $navs.removeClass("curr");
        $(".head-nav li[val='" + $thisMenuBox.attr("id") + "']").addClass("curr");

        $(".menu-box").hide();
        $thisMenuBox.show();

        $dls.removeClass("dl-show");
        $parent.addClass("dl-show");
    }