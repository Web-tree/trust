import {Application} from "./application";

export class ApplicationWrapper {
  secret: String;
  application: Application;

  constructor(secret: String, app: Application) {
    this.secret = secret;
    this.application = app;
  }
}
