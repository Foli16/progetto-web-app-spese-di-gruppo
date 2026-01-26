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

  getGroupDetail(groupId:string | null, myPartId:string | null)
  {
    return this.http.get<GroupDetailGet>("api/groups/"+groupId).subscribe({
      next: (resp) =>
      {
        this.openedGroup = resp;
        this.openedGroup.expenses = this.openedGroup.expenses.map(e => (
          {
          ...e,
          creationTime: new Date(e.creationTime)
          }
        ));
        this.getOpenedGroupBasicInfo(myPartId);
        this.openedGroup.expenses.sort((a,b) => 
          {
            const dateComparation = b.date.localeCompare(a.date);
            return dateComparation == 0 ? b.creationTime.getTime() - a.creationTime.getTime() : dateComparation;
          }
        );
      },
      error: () => alert("errore fatale")
    });
  }

  private getOpenedGroupBasicInfo(myPartId:string | null)
  {
    let singleIdArray = [myPartId];
    return this.http.post<GroupPreviewGet[]>("api/groups/list", singleIdArray).subscribe({
      next: (resp) => 
        {
          this.openedGroup!.basicInfo = resp[0];
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
