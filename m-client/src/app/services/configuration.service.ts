import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Type } from "../domain/type";
import { Observable, of } from "rxjs";
import { Configuration } from "../domain/configuration";

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private http: HttpClient) {
  }

  getConfigurations(): Observable<Configuration[]> {
    return this.http.get<Configuration[]>('/api/configuration')
  }
  createType(request: Configuration): Observable<Configuration> {
    return this.http.post<Configuration>('/api/configuration', request)
  }

}
