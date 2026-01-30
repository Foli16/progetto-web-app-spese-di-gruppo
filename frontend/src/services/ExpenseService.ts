import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {

  constructor(private http:HttpClient, private router:Router) { }

  // addExpense(groupId:string)
  // {
  //   return this.http.post("api/groups/"+groupId+"addexpense", body)
  // }
}
