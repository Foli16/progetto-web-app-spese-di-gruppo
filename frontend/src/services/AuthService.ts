import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/User';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  loggedUser:User | null = null;
  authChecked = false;

  constructor(private http:HttpClient, public router:Router) {
      this.getUserInfo();
   }


  register(username:string, password:string)
  {
    let body = {username, password};

    this.http.post("api/auth/register", body).subscribe({
      next: () => this.router.navigate([""]),
      error: () => alert("password non valida")
    });
  }

  login(username:string, password:string)
  {
    let body = {username, password};

    this.http.post("api/auth/login", body).subscribe({
      next:() =>
      { 
        this.getUserInfo();
        this.router.navigate ([""]);
      },
      error: (error) =>
        alert(error.message)
    });
  }

  getUserInfo()
  {
    this.http.get<User>("api/auth/userinformation").subscribe({
      next: (user: User) =>
        {
          this.loggedUser = user;
          this.authChecked = true;
        },
      error: () =>
        this.authChecked = true
      }
    );
  }

  logout()
  {
    this.loggedUser = null;
    document.cookie = 'token=; Path=/api; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }
}
