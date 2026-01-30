import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroupPreviewGet } from '../model/GroupPreviewGet';
import { LocalParticipantService } from './LocalParticipantService';
import { SpendingGroupPost } from '../model/SpendingGroupPost';
import { Router } from '@angular/router';
import { GroupDetailGet } from '../model/GroupDetailGet';
import { EMPTY, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  groups:GroupPreviewGet[] = [];
  openedGroup:GroupDetailGet | null = null;

  constructor(private http:HttpClient, private localServ:LocalParticipantService, private router:Router) { }

  getGroupList()
  {
    return this.http.post<GroupPreviewGet[]>("api/groups/list", this.localServ.getAll()).subscribe({
      next: (resp) => 
        {
          this.groups = resp;
          this.openedGroup = null;
        },
      error: (error) => alert(error)
    });
  }

  getGroupDetail(groupId:string | null, myPartId:string | null):Observable<GroupDetailGet>
  {
    if(!groupId || !myPartId)
    {
      return EMPTY;
    }
    return this.http.get<GroupDetailGet>("api/groups/"+groupId+"/"+myPartId);
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
