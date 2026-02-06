import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-participants-list',
  imports: [],
  templateUrl: './participants-list.html',
  styleUrl: './participants-list.css',
})
export class ParticipantsList {
  @Input() participants:any;
 }
