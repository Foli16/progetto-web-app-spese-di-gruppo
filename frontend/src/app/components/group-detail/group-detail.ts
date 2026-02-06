import { Component, Input } from '@angular/core';
import { GroupService } from '../../../services/GroupService';
import { GroupPreviewGet } from '../../../model/GroupPreviewGet';
import { GroupDetailGet } from '../../../model/GroupDetailGet';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ExpenseDetail } from "../expense-detail/expense-detail";
import { ParticipantsList } from "../participants-list/participants-list";

@Component({
  selector: 'app-group-detail',
  imports: [ExpenseDetail, ParticipantsList],
  templateUrl: './group-detail.html',
  styleUrl: './group-detail.css',
})
export class GroupDetail {
  
  
  constructor(public serv:GroupService, private route:ActivatedRoute, public router:Router)
  {
    this.getGroupDetail();
  }
  
  getGroupDetail()
  {
    const id = this.route.snapshot.paramMap.get("id");
    const partId = this.route.snapshot.paramMap.get("partId");
    return this.serv.getGroupDetail(id, partId).subscribe({
      next: (resp) =>
      {
        this.serv.openedGroup = resp;
      },
      error: () =>
      {
        alert("errore fatale");
        this.router.navigate(["/"]);
      }
    });
  }

  openExpenseForm(groupId:string, partId:string)
  {
    this.router.navigate(["/group-detail/"+groupId+"/"+partId+"/new-expense"]);
  }
 }
