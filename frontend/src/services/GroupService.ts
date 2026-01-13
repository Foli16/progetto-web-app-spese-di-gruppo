import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SpendingGroupGet } from '../model/SpendingGroupGet';
import { LocalParticipantService } from './LocalParticipantService';
import { SpendingGroupPost } from '../model/SpendingGroupPost';
import { errorContext } from 'rxjs/internal/util/errorContext';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  groups:SpendingGroupGet[] = [];

  constructor(private http:HttpClient, private localServ:LocalParticipantService) { }

  getGroupList()
  {
    return this.http.post<SpendingGroupGet[]>("api/groups/list", this.localServ.getAll()).subscribe({
      next: (resp) => this.groups = resp,
      error: (error) => alert(error)
    });
  }

  createGroup(group:SpendingGroupPost)
  {
    return this.http.post<string>("api/groups/create", group).subscribe
    ({
      next: (id) => 
        {
          if(id == null)
            return;
          this.localServ.save(id);
        },
      error: (error) => alert(error)
    });
  }
}
