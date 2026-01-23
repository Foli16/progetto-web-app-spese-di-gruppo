import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroupPreviewGet } from '../model/GroupPreviewGet';
import { LocalParticipantService } from './LocalParticipantService';
import { SpendingGroupPost } from '../model/SpendingGroupPost';
import { Router } from '@angular/router';
import { GroupDetailGet } from '../model/GroupDetailGet';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  groups:GroupPreviewGet[] = [];
  openedGroup:GroupDetailGet | null = null;
  openedGroupBasicInfo:GroupPreviewGet | null = null;

  constructor(private http:HttpClient, private localServ:LocalParticipantService, private router:Router) { }

  getGroupList()
  {
    return this.http.post<GroupPreviewGet[]>("api/groups/list", this.localServ.getAll()).subscribe({
      next: (resp) => 
        {
          this.groups = resp;
          this.groups.sort((a,b) => a.groupName.localeCompare(b.groupName));
        },
      error: (error) => alert(error)
    });
  }

  getGroupDetail(group:GroupPreviewGet)
  {
    this.openedGroupBasicInfo = group;
    return this.http.get<GroupDetailGet>("api/groups/"+group.groupId).subscribe({
      next: (resp) =>
      {
        this.openedGroup = resp;
        this.router.navigate(["/group-detail"])
      },
      error: () => alert("errore fatale")
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
