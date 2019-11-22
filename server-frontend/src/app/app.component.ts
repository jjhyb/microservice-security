import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'yibo frontend microservice security';
  authenticated = false;
  credentials = {username:'', password:''};
  order = {};

  constructor(private http: HttpClient, private cookieService: CookieService){
    this.http.get('api/user/me').subscribe(data => {
      if(data){
        this.authenticated = true;
      }

      if(!this.authenticated){
        window.location.href = 'http://auth.yibo.com:9090/oauth/authorize?' +
          'client_id=serverFrontend&' +
          'redirect_uri=http://frontend.yibo.com:8080/oauth/callback&' + //认证中心授权码发送的地址
          'response_type=code&' + //采用授权码模式完成整个授权
          'state=abc';
      }
    });
  }

  getOrder(){
    this.http.get('api/order/orders/1').subscribe(data => {
      this.order = data;
    },() => {
      alert('get order fail');
    });
  }

  login(){
    this.http.post('login',this.credentials).subscribe(() => {
      this.authenticated = true;
    },() => {
      alert('login fail');
    });
  }

  logout(){
    //当access_token失效掉
    this.cookieService.delete('yibo_access_token','/','yibo.com');
    this.cookieService.delete('yibo_refresh_token','/','yibo.com');
    this.http.post('logout',this.credentials).subscribe(() => {
      //本地退出后，发送退出请求到认证服务器，并且认证中心退出后跳回到 frontend服务的认证主页是上
      window.location.href = 'http://auth.yibo.com:9090/logout?redirect_uri=http://frontend.yibo.com:8080';
      this.authenticated = false;
    },() => {
      alert('logout fail');
    });
  }
}
