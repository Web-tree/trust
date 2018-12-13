import {Component, OnInit} from '@angular/core';
import {ApplicationService} from '../_services/application.service';
import {FormControl, FormGroup} from "@angular/forms";
import {AlertService} from '../_services/alert.service';
import {Application} from '../_models/application';
import {ApplicationWrapper} from '../_models/applicaionWrapper';

@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {

  constructor(private service: ApplicationService,
              private alertService: AlertService) {
    this.initAppForm();
  }

  applications;
  applicationForm: FormGroup;
  newNameForm: FormGroup;
  newNameFormAppId;
  newSecretInfoAppId;
  newSecret;
  newApp: ApplicationWrapper;
  showNewApp;

  initAppForm() {
    this.applicationForm = new FormGroup({
      name: new FormControl()
    });
  }

  initChangeNameForm() {
    this.newNameForm = new FormGroup({
      newName: new FormControl()
    });
  }

  ngOnInit() {
    this.getList();
  }

  getList() {
    this.service.getList().subscribe(
      (app) => {
        this.applications = app;
      },
      (error) => {
        this.alertService.error(error.error.error);
      }
    );
  }

  delete(id: string) {
    this.service.delete(id).subscribe(
      (response) => {
        const index = this.applications.findIndex(app => app.id == id);
        this.applications.splice(index, 1);
      },
      (error) => {
        this.alertService.error(error.error.error);
      });
  }

  resetSecret(id: string) {
    this.service.resetSecret(id).subscribe(
      (secret) => {
        this.newSecret = secret;
        this.newSecretInfoAppId = id;
      },
      error => {
        this.alertService.error(error.error.error);
      }
    );
  }

  openChangeNameInputFor(id: string) {
    if (id == this.newNameFormAppId) {
      this.newNameFormAppId = null;
      this.newNameForm = null;
    }
    else {
      this.initChangeNameForm();
      this.newNameFormAppId = id;
    }
  }

  changeName(id: string) {
    const name: string = this.newNameForm.value.newName;
    this.service.changeName(id, name).subscribe(
      (response) => {
        let app: Application = this.applications.find(app => app.id == id);
        app.name = name;
        this.newNameForm = null;
        this.newNameFormAppId = null;
      },
      error => {
        this.alertService.error(error.error.error);
      }
    );
  }

  add() {
    this.service.add(this.applicationForm.value.name).subscribe(
      (newApplication) => {
        this.applications.push(newApplication.application);
        this.showNewApp = true;
        this.newApp = newApplication;
        this.initAppForm();
      },
      error => {
        this.alertService.error(error.error.error);
      }
    );
  }

  closeNewSecretInfo() {
    this.newSecret = null;
    this.newSecretInfoAppId = null;
  }

  closeNewAppInfo() {
    this.showNewApp = false;
    this.newApp = null;
  }
}
