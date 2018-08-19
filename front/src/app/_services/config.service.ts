import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  constructor() { }
  private backEndUrl:String = "http://localhost:9000";

  getBackUrl(){
    return this.backEndUrl;
  }

  getProviderApp(providerId) {

  }
}
