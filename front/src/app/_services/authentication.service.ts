import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map'
import {TokenService} from "./token.service";
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "./config.service";

@Injectable()
export class AuthenticationService {
  constructor(private http: HttpClient,
              private tokenService: TokenService,
              private config: ConfigService) {
  }

  socialLogin(provider: string, token: string) {
    let body = {
      provider: provider,
      token: token
    };
    return this.http.post(this.config.getBackUrl() + '/rest/social/login', body, {responseType: 'text'});
  }

  logout() {
    this.tokenService.removeToken();
  }

  isAuthorized(): boolean {
    return this.tokenService.tokenExists();
  }
}
