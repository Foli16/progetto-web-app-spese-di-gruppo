import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http:HttpClient, public router:Router) { }

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
      next:() => this.router.navigate ([""]),
      error: () => alert("errore fatale")
    });
  }

  logout()
  {
    document.cookie = 'token=; Path=/api; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }
}
