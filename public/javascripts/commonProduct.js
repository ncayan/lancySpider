 //获取复选框的值，逗号分隔
function getSelectedIds(checkedIds) {
	
	var ids = "";
	for(var i=0; i<checkedIds.length; i++){
	    ids += ""+checkedIds[i].value+",";
	}
	return ids.substring(0,ids.lastIndexOf(","));
}
//图片拖拽
 
function dragImgComm(){
	var startX,endX,startY,endY;
    var srcId,startRegion;
    var srcLeft=0,srcTop=0,destLeft=0,destTop=0;
  	$('img[name="dragImg"]').draggable({
		
		onStartDrag:function(e){
  			
  			srcId = $(e.target).attr("id");
  			srcLeft = $(e.target).position().left;
			srcTop = $(e.target).position().top;
			startX = e.pageX - $(e.target).position().left;
        	startY = e.pageY - $(e.target).position().top;
        		
			var tdHeight = $(e.target).parent().height();
			var startLeft = $(e.target).css("left");
			var startTop = $(e.target).css("top");
			startRegion = startwhichRegion(e,startLeft,startTop,tdHeight);
			console.log("startRegion="+startRegion);
		},
		onStopDrag:function(e){
			endX = e.pageX - $(e.target).position().left;
        	endY = e.pageY - $(e.target).position().top;
        	
			destLeft = $(e.target).css("left");
			destTop = $(e.target).css("top");
			var src = $(e.target).attr("src");
			
			var tdHeight = $(e.target).parent().height();
			var tdWidth = $(e.target).parent().width();
			var region = whichRegion(e,destLeft,destTop,tdHeight,tdWidth,srcLeft,srcTop);
			console.log("region="+region);
			
			$("#pic"+region).val(src);
			if($("#cpic"+region).attr("src")!=""){
				$("#cpic"+region).css({
					position:"absolute",
					left:srcLeft,
					top:srcTop
				});
			}
			
			$("#pic"+startRegion).val($("#cpic"+region).attr("src"));
			$("#cpic"+region).attr("id",srcId);
			$(e.target).attr("id","cpic"+region);
			
		}
	});
}

//确定拖拽的区域
function whichRegion(e,dragLeft,dragTop,tdHeight,tdWidth,srcLeft,srcTop){
				dragLeft = parseInt(dragLeft);
				dragTop = parseInt(dragTop);
				
				var column = 0,row = 0;
				var pic1PLeft = $("#td1").position().left;
				var pic2PLeft = $("#td2").position().left;
				var pic3PLeft = $("#td3").position().left;
				var pic4PLeft = $("#td8").length<=0?null:$("#td8").position().left;
				
				if(dragLeft<=pic1PLeft||(dragLeft>pic1PLeft&&dragLeft<=pic2PLeft)){
					moveToX(dragLeft,srcLeft,tdWidth,0,e,dragTop,pic1PLeft);
					column = 0;
				}else if(dragLeft>pic2PLeft&&dragLeft<=pic3PLeft){
					moveToX(dragLeft,srcLeft,tdWidth,0,e,dragTop,pic2PLeft);
					column = 1;
				}else if(pic4PLeft!=null){
					if(dragLeft>pic3PLeft&&dragLeft<=pic4PLeft){
						moveToX(dragLeft,srcLeft,tdWidth,0,e,dragTop,pic3PLeft);
						column = 2;
					}else{
						moveToX(dragLeft,srcLeft,tdWidth,0,e,dragTop,pic4PLeft);
						column = 3;
					}
				}else{
					moveToX(dragLeft,srcLeft,tdWidth,0,e,dragTop,pic3PLeft);
					column = 2;
				}
					
				
				var pic1PTop = $("#td1").position().top;
				var pic2PTop = $("#td4").position().top;
				var pic3PTop = $("#td7").position().top;
				
				if(dragTop<=pic1PTop||(dragTop>=pic1PTop&&dragTop<=pic2PTop)){
					moveToY(dragTop,srcTop,tdHeight,0,e,dragLeft,pic1PTop);
					row = 0;
				}else if(dragTop>=pic2PTop&&dragTop<=pic3PTop){
					moveToY(dragTop,srcTop,tdHeight,1,e,dragLeft,pic2PTop);
					row = 1;
				}else {
					moveToY(dragTop,srcTop,tdHeight,2,e,dragLeft,pic3PTop);
					row = 2;
				}
				var region = 1;
				if(column==3){
					if(row==3){
						var cell = (dragTop-pic4PTop)%tdHeight;
						row += cell;
					}
					region = 13+row+1;
				}else{
					region = row*3+column+1;
				}
				console.log("row="+row+",cl="+column);
				return region;
}
//确定拖拽的区域
function startwhichRegion(e,dragLeft,dragTop,tdHeight){
				dragLeft = parseInt(dragLeft);
				dragTop = parseInt(dragTop);
				
				var column = 0,row = 0;
				var pic1PLeft = $("#td1").position().left;
				var pic2PLeft = $("#td2").position().left;
				var pic3PLeft = $("#td3").position().left;
				var pic4PLeft = $("#td8").length<=0?null:$("#td8").position().left;
				
				if(dragLeft<=pic1PLeft||(dragLeft>pic1PLeft&&dragLeft<pic2PLeft)){					
					column = 0;
				}else if(dragLeft>pic2PLeft&&dragLeft<pic3PLeft){
					column = 1;
				}else if(pic4PLeft!=null){
					if(dragLeft>pic3PLeft&&dragLeft<pic4PLeft){
						column = 2;
					}else{
						column = 3;
					}
				}else
					column = 2;
				
				var pic1PTop = $("#td1").position().top;
				var pic2PTop = $("#td4").position().top;
				var pic3PTop = $("#td7").position().top;
				
				if(dragTop<=pic1PTop||(dragTop>=pic1PTop&&dragTop<pic2PTop)){
					row = 0;
				}else if(dragTop>=pic2PTop&&dragTop<pic3PTop){
					row = 1;
				}else {
					row = 2;
				}
				var region = 1;
				if(column==3){
					if(row==3){
						var cell = (dragTop-pic4PTop)%tdHeight;
						row += cell;
					}
					region = 13+row+1;
				}else{
					region = row*3+column+1;
				}
				return region;
}
function moveToY(destTop,srcTop,tdHeight,row,e,left,top){
	var y = parseInt(destTop)-parseInt(srcTop);
	var targetTop;
	var h1 = parseInt(tdHeight)+parseInt(top);
	console.log("dest="+destTop);
	console.log("p1234 top="+top);
	console.log("高度和="+h1);
	
	if(parseInt(destTop)>h1){
			targetTop = h1;
	}else
		targetTop = parseInt(top);
	$(e.target).css({
			position:"absolute",
			left:left,
			top:targetTop
	});
}
function moveToX(destLeft,srcLeft,tdWidth,col,e,dragTop,pleft){
	var x = parseInt(destLeft)-parseInt(srcLeft);
	var targetLeft;
	var h1 = parseInt(tdWidth)+parseInt(pleft);
	console.log("dest="+destLeft);
	console.log("p1234 left="+pleft);
	console.log("宽度和="+h1);
	
	if(parseInt(destLeft)>h1){
			targetLeft = h1;
	}else
		targetLeft = parseInt(pleft);
	console.log("target="+targetLeft);
	$(e.target).css({
			position:"absolute",
			left:parseInt(targetLeft),
			top:dragTop
	});
}