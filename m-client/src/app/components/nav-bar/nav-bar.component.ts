import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Authentication } from "../../domain/authentication";

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {

  @Input() isMobile = false;
  @Input() user: Authentication | null = null;
  @Input() currentLanguage: string = '';
  @Input() languages: string[] = []
  @Output() logout = new EventEmitter<boolean>()
  @Output() changePassword = new EventEmitter()
  @Output() changeLanguage = new EventEmitter<string>()

  @ViewChild("sidenav") sidenav: any
  isExpanded = false;

  onExpand() {
    this.sidenav.toggle()
    this.isExpanded = !this.isExpanded
  }
}
