import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/User';
import { CookieService } from 'ngx-cookie-service';
import { BehaviorSubject, Observable } from 'rxjs';
import { GroupService } from './GroupService';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  

  private userSubject = new BehaviorSubject<User | null | undefined>(undefined);
  user$: Observable<User | null | undefined> = this.userSubject.asObservable();

  constructor(private http:HttpClient, public router:Router, private serv:GroupService) {
      this.getUserInfo();
   }

  get loggedUser(): User | null {
    return this.userSubject.value ?? null;
  }

  register(username:string, password:string)
  {
    let body = {username, password};

    this.http.post("api/auth/register", body).subscribe({
      next: () => 
        {
          this.getUserInfo();
          this.router.navigate([""]);
        },
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
      next: (user) =>
        {
          this.userSubject.next(user)
        },
      error: () =>
        this.userSubject.next(null)
      }
    );
  }

  logout()
  {
    this.userSubject.next(null);
    document.cookie = 'token=; Path=/api; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    this.serv.getGroupList();
    this.router.navigate(["/"]);
  }
}
