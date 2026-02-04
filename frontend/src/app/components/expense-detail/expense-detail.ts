import { Component, input, Input } from '@angular/core';
import { GroupDetailGet } from '../../../model/GroupDetailGet';

@Component({
  selector: 'app-expense-detail',
  imports: [],
  templateUrl: './expense-detail.html',
  styleUrl: './expense-detail.css',
})
export class ExpenseDetail {
  @Input() expense:any
  @Input() index?:number
 }
