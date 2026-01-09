import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Prova } from '../model/Prova';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  groupNames:Prova[] = [];

  constructor(private http:HttpClient) { }

  getGroupList()
  {
    return this.http.get<Prova[]>("api/group/list").subscribe({
      next: (resp) => this.groupNames = resp,
      error: () => alert("error")
    });
  }
}
