    //codes.js     
    system = require('system')     
    address = system.args[1];//获得命令行第二个参数 接下来会用到 
//	proxy = system.args[2]; //获得命令行第三个参数（代理IP）
//	cookieStr = system.args[3];
	/*if(proxy != "x"){
		var p = proxy.split(":");
            p_ip = p[0];
            p_port = p[1];
            phantom.setProxy(p_ip, p_port, "HTTP");
	}*/
	var login1 = {
		    'name': 'TY_SESSION_ID',
		    'value': '61b53616-06ec-4f7e-9487-09334f2f2a4f',
		    'path': '/',
		    'domain':'s.wxb.com.cn'     //这里！ 这里
	}
	phantom.addCookie(login1);
    var page = require('webpage').create(); 
	page.customHeaders = {
		    "UA": "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"
	};
    var url = address;     
    //console.log(url);     
    page.open(url, function (status) {     
        //Page is loaded!     
        if (status !== 'success') {     
            console.log('Unable to post!');     
        } else {     
        	//此处的打印，是将结果以流的形式output到java中，java通过InputStream可以获取该输出内容       
            console.log(page.content);     
        }        
        phantom.exit();     
    });  