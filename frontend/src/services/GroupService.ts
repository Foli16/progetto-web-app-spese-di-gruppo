import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SpendingGroupGet } from '../model/SpendingGroupGet';
import { LocalParticipantService } from './LocalParticipantService';
import { SpendingGroupPost } from '../model/SpendingGroupPost';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  groups:SpendingGroupGet[] = [];

  constructor(private http:HttpClient, private localServ:LocalParticipantService, private router:Router) { }

  getGroupList()
  {
    return this.http.post<SpendingGroupGet[]>("api/groups/list", this.localServ.getAll()).subscribe({
      next: (resp) => 
        {
          this.groups = resp;
          this.groups.sort((a,b) => a.groupName.localeCompare(b.groupName));
        },
      error: (error) => alert(error)
    });
  }

  createGroup(group:SpendingGroupPost)
  {
    return this.http.post<string>("api/groups/create", group).subscribe
    ({
      next: (id) => 
        {
          this.router.navigate(["/"]);
          if(id == null)
            return;
          this.localServ.save(id);
        },
      error: (error) => alert(error)
    });
  }
}
