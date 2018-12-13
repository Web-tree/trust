import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ConfigService} from "./config.service";
import {ApplicationWrapper} from '../_models/applicaionWrapper';
import {Application} from '../_models/application';
import {Observable} from "rxjs/internal/Observable";


@Injectable({
  providedIn: 'root'
})

export class ApplicationService {

  constructor(private http: HttpClient,
              private config: ConfigService) {
  }

  public getList(): Observable<Application[]> {
    return this.http.get<Application[]>(this.config.getBackUrl() + '/rest/app/list');
  }

  public delete(id: String): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(this.config.getBackUrl() + '/rest/app/' + id);
  }

  public resetSecret(id: String): Observable<String> {
    return this.http.post<String>(this.config.getBackUrl() + '/rest/app/reset/'+ id,null);
  }

  public changeName(id: String, name: String): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(this.config.getBackUrl() + '/rest/app/' + id, name);
  }

  public add(name: String): Observable<ApplicationWrapper> {
    return this.http.post<ApplicationWrapper>(this.config.getBackUrl() + '/rest/app', name);
  }
}
