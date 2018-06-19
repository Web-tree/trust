import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "./config.service";
import {User} from "../_models/user";
import {Observable} from "rxjs/internal/Observable";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private httpClient: HttpClient,
    private configService: ConfigService) {}

  create(user: User): Observable<User>{
    return this.httpClient.post<User>(this.configService.getBackUrl() + "/rest/user/register", user);
  }
}

