import { Component, OnInit } from '@angular/core';
import { Configuration } from "../../domain/configuration";
import { ConfigurationService } from "../../services/configuration.service";
import { ConfigurationType } from "../../domain/configuration-type";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: './settings.page.html',
  styleUrls: ['./settings.page.css']
})
export class SettingsPage implements OnInit {
  configurations: Configuration[] = []
  configMap: { [key: string]: Configuration } = {}

  richTextConfigMap: { [key: string]: Configuration } = {}
  textConfigMap: { [key: string]: Configuration } = {}

  constructor(private service: ConfigurationService, private toast: NgToastService, private translate: TranslateService,) {
  }

  ngOnInit(): void {
    this.service.getConfigurations().subscribe(
      result => {
        this.configurations = result
        this.configMap = Object.fromEntries(result.map(it => [it.key, it]))
        let { richTextMap, textMap } = result.reduce((maps, it) => {
          if (it.type === ConfigurationType.RICH_TEXT) {
            maps.richTextMap[it.key] = it;
          } else {
            maps.textMap[it.key] = it;
          }
          return maps;
        }, { richTextMap: {} as { [key: string]: Configuration }, textMap: {} as { [key: string]: Configuration } });
        this.richTextConfigMap = richTextMap;
        this.textConfigMap = textMap;
      }
    )
  }

  getRichTextConfigs(): Configuration[] {
    return Object.values(this.richTextConfigMap)
  }

  getTextConfigs(): Configuration[] {
    return Object.values(this.textConfigMap)
  }

  onSave(key: string) {
    if (this.configMap[key]) {
      this.service.createType(this.configMap[key]).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant(key)} ${this.translate.instant("updated_successfully")}`)
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      )
    }
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
