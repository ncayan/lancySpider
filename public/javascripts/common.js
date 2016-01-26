$(function(){  
    // 设置jQuery Ajax全局的参数  
    $.ajaxSetup({  
        error: function(jqXHR, textStatus, errorThrown){  
            switch (jqXHR.status){  
                case(500):
                	msg_show("出错了!","服务器系统内部错误");
                    break;  
                case(401):
                	msg_show("出错了!","未登录");
                    break;
                case(403):
                	msg_show("出错了!","无权限执行此操作");
                    break;  
                case(408):
                	msg_show("出错了!","请求超时");
                    break;  
                default:
                	msg_show("出错了!","未知错误");
            };
            $.messager.progress('close'); 
        }
    });  
});  
function msg_show(title,msg){
	$.messager.show({
        title: title,
        timeout:3000,
        msg: msg,
        style:{
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:''
        }
    });
} 

function fileUpload(form, action_url, div_id,chkFileType,callback) {
				    // Create the iframe...
					    if(!chkFileType()){
					    	return ;
					    }
					    	
					    	
					    var iframe = document.createElement("iframe");
					    iframe.setAttribute("id", "upload_iframe");
					    iframe.setAttribute("name", "upload_iframe");
					    iframe.setAttribute("width", "0");
					    iframe.setAttribute("height", "0");
					    iframe.setAttribute("border", "0");
					    iframe.setAttribute("style", "width: 0; height: 0; border: none;");
					 
					    // Add to document...
					    form.parentNode.appendChild(iframe);
					    window.frames['upload_iframe'].name = "upload_iframe";
					 
					    iframeId = document.getElementById("upload_iframe");
					 
					    // Add event...
					    var eventHandler = function () {
					 
					            if (iframeId.detachEvent) iframeId.detachEvent("onload", eventHandler);
					            else iframeId.removeEventListener("load", eventHandler, false);
					 
					            // Message from server...
					            if (iframeId.contentDocument) {
					                content = iframeId.contentDocument.body.innerHTML;
					            } else if (iframeId.contentWindow) {
					                content = iframeId.contentWindow.document.body.innerHTML;
					            } else if (iframeId.document) {
					                content = iframeId.document.body.innerHTML;
					            }
					 
					            document.getElementById(div_id).innerHTML = content;
					 
					            // Del the iframe...
					            setTimeout('iframeId.parentNode.removeChild(iframeId)', 250);
					            callback();//回调函数
					        }
					 
					    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
					    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);
					 
					    // Set properties of form...
					    form.setAttribute("target", "upload_iframe");
					    form.setAttribute("action", action_url);
					    form.setAttribute("method", "post");
					    form.setAttribute("enctype", "multipart/form-data");
					    form.setAttribute("encoding", "multipart/form-data");
					 
					    // Submit the form...
					    form.submit();
					 	$("#importReport").append("<div id='progressbar'></div>");
					    //document.getElementById(div_id).innerHTML = "Uploading...";
					    $("#progressbar").progressbar({
							value: false
						});
				
}
//获取复选框的值，逗号分隔
function getSelectedIds(checkedIds) {
	
	var ids = "";
	for(var i=0; i<checkedIds.length; i++){
	    ids += ""+checkedIds[i].value+",";
	}
	return ids.substring(0,ids.lastIndexOf(","));
}
function getSeason(nowMonth){
	if(nowMonth<=3)
		return 1;
	else if(nowMonth<=6)
		return 2;
	else if(nowMonth<=9)
		return 3;
	else 
		return 4;
}
//date format:"yyyy-MM-dd"
Date.prototype.format = function(format)
{
	 var o = {
	 "M+" : this.getMonth()+1, //month
	 "d+" : this.getDate(),    //day
	 "h+" : this.getHours(),   //hour
	 "m+" : this.getMinutes(), //minute
	 "s+" : this.getSeconds(), //second
	 "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
	 "S" : this.getMilliseconds() //millisecond
	 }
	 if(/(y+)/.test(format)) 
		 format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
	 for(var k in o)
		 if(new RegExp("("+ k +")").test(format))
	 format = format.replace(RegExp.$1,
	 RegExp.$1.length==1 ? o[k] :
	 ("00"+ o[k]).substr((""+ o[k]).length));
	 return format;
}
//图片拖拽
function makeElementAsDragAndDrop(elem) {
    $(elem).draggable({
        revert: "invalid",
        cursor: "move",
        helper: "clone"
    });
    $(elem).droppable({
    	drop: function(event, ui) {
            var $dragElem = $(ui.draggable).clone().replaceAll(this);
            $(this).replaceAll(ui.draggable);
            makeElementAsDragAndDrop(this);
            makeElementAsDragAndDrop($dragElem);
        }
    });
}
function getTreeData(url){
	$.get(url,
	      function(data) {
			var sourceTree =
            {
                datatype: "json",
                datafields: [
                    { name: 'id' },
                    { name: 'parentid' },
                    { name: 'text' },
                    { name: 'value' }
                ],
                id: 'id',
                localdata: data
            };

			 var dataAdapter = new $.jqx.dataAdapter(sourceTree);
             dataAdapter.dataBind();
             var records = dataAdapter.getRecordsHierarchy('id', 'parentid', 'items', [{ name: 'text', map: 'label'}]);
             $("#dropDownButton").jqxDropDownButton({ width: 218, height: 25});
             $('#parentId').on('select', function (event) {
                 var args = event.args;
                 var item = $('#parentId').jqxTree('getItem', args.element);
                 var dropDownContent = '<div style="position: relative; margin-left: 3px; margin-top: 5px;">' + item.label + '</div>';
                 $("#dropDownButton").jqxDropDownButton('setContent', dropDownContent);
             });
            $("#parentId").jqxTree({source: records, width: 218, height: 220});
            
	      }
     );
	
}
function dragImgComm(imgLength){
	
    var srcId,startRegion;
    var srcLeft=0,srcTop=0,destLeft=0,destTop=0;
    var td1Top = $("#td1").position().top;
    var td1Left = $("#td_title").position().left;
    var td_titleW = $("#td_title").width();
  	$('img[name="dragImg"]').draggable({
		
		onStartDrag:function(e){
  			
  			srcId = $(e.target).attr("id");
  			var num = parseInt(srcId.substr(4));
  			
  			srcLeft = $(e.target).position().left;
			srcTop = $(e.target).position().top;
			
			var tdHeight = $(e.target).parent().height();
			var tdWidth = $(e.target).parent().width();
			var ptop = $(e.target).parent().position().top;
			var crossRow = parseInt((ptop-td1Top)/tdHeight)+1;
			var crossCol = num-(crossRow-1)*3;
			var startLeft = $(e.target).css("left");
			var startTop = $(e.target).css("top");
			startRegion = num;
			console.log("startRegion="+startRegion);
		},
		onStopDrag:function(e){
			
			destLeft = $(e.target).css("left");
			destTop = $(e.target).css("top");
			var src = $(e.target).attr("src");
			
			
			var tdHeight = $(e.target).parent().height();
			var tdWidth = $(e.target).parent().width();
			
			var crossRow = parseInt((parseInt(destTop)-td1Top)/tdHeight);
			var crossCol = parseInt((parseInt(destLeft)-td1Left)/(tdWidth+td_titleW));
			//var region = whichRegion(e,destLeft,destTop,tdHeight,tdWidth,srcLeft,srcTop);
			var region = crossRow*3+crossCol+1;
			console.log("row,col="+crossRow+","+crossCol+",stop region="+region);
			var intMoveToX = $("#td"+(crossCol+1)).position().left;
			var intMoveToY = $("#td"+region).position().top;
			console.log("x="+intMoveToX+",y="+intMoveToY);
			$(e.target).css({
					position:"absolute",
					left:intMoveToX,
					top:intMoveToY
			});
			if(startRegion!=region){
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
		}
	});
}
function loadSuolueImg(imgLength,totalCell,imgs){
	 				if(imgLength>totalCell){
				        	  var trs = "";
				        	  var j = imgLength-totalCell;
				        	  var td_title = "<td width='6%' nowrap='nowrap' class='td_title'></td>";
				        	  var row_tr = "";
				        	  for(var i=1;i<=j;i++){
				        		  var id = totalCell+i;
				        		  var tr = "<tr>";
				        		  
				        		  var td = td_title+"<td id='td"+id+"' height='90px' width:'90px'><div name='dragDiv'><img id='cpic"+id+"' name='dragImg'  src='"+imgs[id-1]+"'></div></td>";
				        		   if(i%5==1){
				        				row_tr += tr+td;
				        		   }else{
				        			   row_tr += td;
				        		   }
				        		   if((i==j)&&(i%5!=0)){
				        			  
				        			   var leftCol = 5-(i%5);
				        			   for(var k=1;k<=leftCol;k++){
				        				    var idInc = id+k;
				        				   row_tr += td_title+"<td id='td"+idInc+"' height='90px' width:'90px'><div name='dragDiv'><img id='cpic"+idInc+"' name='dragImg'  src=''></div></td>"
				        			   }
				        		   }
				        		   if(i%5==0){
				        				row_tr += "</tr>";
				        		   }
				        	  }
				        	 $("#table_1").append(row_tr);
				          }
}
function loadEditSuolueImg(imgLength,totalCell,imgs){
	 				if(imgLength>totalCell){
				        	  var trs = "";
				        	  var j = imgLength-totalCell;
				        	  var td_title = "<td width='6%' nowrap='nowrap' class='td_title'></td>";
				        	  var row_tr = "";
				        	  for(var i=1;i<=j;i++){
				        		  var id = totalCell+i;
				        		  var tr = "<tr>";
				        		  
				        		  var td = td_title+"<td id='td"+id+"' height='90px' width:'90px'><div name='dragDiv'><img id='cpic"+id+"' name='dragImg'  src='"+imgs[i-1]+"'></div></td>";
				        		   if(i%5==1){
				        				row_tr += tr+td;
				        		   }else{
				        			   row_tr += td;
				        		   }
				        		   if((i==j)&&(i%5!=0)){
				        			  
				        			   var leftCol = 5-(i%5);
				        			   for(var k=1;k<=leftCol;k++){
				        				    var idInc = id+k;
				        				   row_tr += td_title+"<td id='td"+idInc+"' height='90px' width:'90px'><div name='dragDiv'><img id='cpic"+idInc+"' name='dragImg'  src=''></div></td>"
				        			   }
				        		   }
				        		   if(i%5==0){
				        				row_tr += "</tr>";
				        		   }
				        	  }
				        	 $("#table_1").append(row_tr);
				          }
}