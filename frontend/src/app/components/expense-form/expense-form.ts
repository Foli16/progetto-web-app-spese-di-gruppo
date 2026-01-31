import { Component } from '@angular/core';
import { ExpensePost } from '../../../model/ExpensePost';
import { ExpenseService } from '../../../services/ExpenseService';
import { FormsModule } from '@angular/forms';
import { GroupService } from '../../../services/GroupService';
import { ActivatedRoute, Router } from '@angular/router';

type ExpenseRow = {
  participantId: string | null;
  tempShare: number | null;
  tempPaid: number | null;
};
@Component({
  selector: 'app-expense-form',
  imports: [FormsModule],
  templateUrl: './expense-form.html',
  styleUrl: './expense-form.css',
})
export class ExpenseForm {
  expensePost:ExpensePost = {title:"", date:"", expenseParticipants:{}};
  expenseRows: ExpenseRow[] = [{ participantId: null, tempShare:null, tempPaid:null }];
  urlGroupId:string | null = null;
  urlMyParticipantId:string | null = null;

  constructor(public eServ:ExpenseService, public gServ:GroupService, private route:ActivatedRoute, private router:Router)
  {
    this.getGroupInfo();
  }

  getGroupInfo()
  {
    this.urlGroupId = this.route.snapshot.paramMap.get("id");
    this.urlMyParticipantId = this.route.snapshot.paramMap.get("partId");
    if(!this.gServ.openedGroup)
    {
      this.gServ.getGroupDetail(this.urlGroupId, this.urlMyParticipantId).subscribe(
          (resp) =>
          {
            this.gServ.openedGroup = resp;
          }
        );
    }
  }

  salva()
  {
    if(this.fillCheck())
    {
      this.fillExpenseParticipants();
      return this.eServ.addExpense(this.gServ.openedGroup!.groupId, this.expensePost).subscribe(
        () => this.router.navigate(["/group-detail", this.urlGroupId, this.urlMyParticipantId])
      );
    }
    return;
  }

  fillExpenseParticipants()
  {
    for(const row of this.expenseRows)
    {
      if(row.participantId)
        this.expensePost.expenseParticipants[row.participantId] = {paidAmount:row.tempPaid, share:row.tempShare};
    }
  }

  addExpenseParticipant()
  {
    if(this.maxParticipantCheck())
      this.expenseRows.push({ participantId: null, tempShare:null, tempPaid:null });
  }

  maxParticipantCheck()
  {
    return this.expenseRows.length < this.gServ.openedGroup!.participants.length;
  }

  onSelectChange(partId:string | null, index:number)
  {
    if(!partId)
      return;
    for(let i = 0; i < this.expenseRows.length; i++)
    {
      if(this.expenseRows[i].participantId === partId && i != index)
        this.expenseRows[i].participantId = null;
    }

  }

  removeExpenseParticipant(index:number)
  {
    if(index !== 0)
      this.expenseRows.splice(index, 1);
  }

  fillCheck()
  {
    return this.expensePost.title && this.expensePost.title.trim().length > 0 && this.expensePost.date &&
    this.expenseRows.every(r => r.participantId && r.tempPaid && r.tempShare && r.tempPaid > 0 && r.tempShare > 0);
  }
 }
