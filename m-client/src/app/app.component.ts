import { Component } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";
import { DomSanitizer } from "@angular/platform-browser";
import { MatIconRegistry } from "@angular/material/icon";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Mushroom';

  constructor(public translate: TranslateService,
              private matIconRegistry: MatIconRegistry,
              private domSanitzer: DomSanitizer,) {
    translate.addLangs(['en', 'sv'])
    translate.setDefaultLang('sv')

    this.matIconRegistry.addSvgIcon(
      'mushroom',
      this.domSanitzer.bypassSecurityTrustResourceUrl('./assets/icons/mushroom.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'mushroom_white',
      this.domSanitzer.bypassSecurityTrustResourceUrl('./assets/icons/mushroom_white.svg')
    );
  }

}
