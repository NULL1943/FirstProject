$(document).ready(
		function() {

			
			// 点击地铁站
			$("ellipse").click(
					function() {

						alert("点击地铁站,ID=" + this.id);
						var top = $(this).offset().top+$(this).height();
						var left = $(this).offset().left;
						console.log("top=" + top);
						console.log("left=" + left);
						
					});
			
			
			// 点击换乘站
			$(".transpoint").click(
					function() {
						/*
						layer.msg('点击换乘站,ID=' + this.id, {
						    time: 20000, //20s后自动关闭
						    btn: ['明白了', '知道了']
						  });
						*/
						
						//tips层
						layer.tips('点击换乘站,ID='+this.id, '#'+this.id);
						
						
						
					});
			
			 $(window).resize(function(){
			      resizer($(this));  //do sonrthing
			      layer.msg('缩放中...', {
					    time: 20000, //20s后自动关闭
					    btn: ['明白了', '知道了']
					  });
			  });
			
		});




