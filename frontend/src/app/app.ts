import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GroupList } from "./components/group-list/group-list";
import { Navbar } from "./components/Navbar/Navbar";
import { GroupForm } from "./components/group-form/group-form";
import { AuthService } from '../services/AuthService';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, AsyncPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(public serv:AuthService)
  {}
  protected readonly title = signal('ProgettoIndividualeFrontend');
}
