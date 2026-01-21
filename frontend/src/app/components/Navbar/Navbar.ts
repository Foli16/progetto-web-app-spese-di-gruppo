import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from "@angular/router";
import { AuthService } from '../../../services/AuthService';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './Navbar.html',
  styleUrl: './Navbar.css'
})
export class Navbar {
  constructor(public serv:AuthService)
  {}
 }
