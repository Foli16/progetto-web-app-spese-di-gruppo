import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ExpensePost } from '../model/ExpensePost';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {

  constructor(private http:HttpClient, private router:Router) { }

  addExpense(groupId:string, body:ExpensePost)
  {
    return this.http.post("api/groups/"+groupId+"/expenses/add", body);
  }
}
