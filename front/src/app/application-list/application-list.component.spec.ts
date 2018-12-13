import {async, ComponentFixture, ComponentFixtureAutoDetect, TestBed} from '@angular/core/testing';

import {ApplicationListComponent} from './application-list.component';
import {HttpClientModule, HttpResponse} from '@angular/common/http';
import {RouterTestingModule} from "@angular/router/testing";
import {ReactiveFormsModule} from '@angular/forms';
import {AlertService} from "../_services/alert.service";
import {Subject} from 'rxjs/internal/Subject';
import {ApplicationService} from "../_services/application.service";
import {Application} from "../_models/application";
import {of} from 'rxjs/internal/observable/of';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';
import {ApplicationWrapper} from "../_models/applicaionWrapper";

describe('ApplicationListComponent', () => {
  let component: ApplicationListComponent;
  let fixture: ComponentFixture<ApplicationListComponent>;
  let appService: ApplicationService;
  let appList: Application[];
  let submitEl: DebugElement[];
  let inputEl: DebugElement[];
  let buttons: DebugElement[];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, ReactiveFormsModule],
      declarations: [ApplicationListComponent],
      providers: [AlertService, Subject, ApplicationService, {provide: ComponentFixtureAutoDetect, useValue: true}]
    })
      // .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationListComponent);
    component = fixture.componentInstance;

    appService = TestBed.get(ApplicationService);
    submitEl = fixture.debugElement.queryAll(By.css('form'));
    inputEl = fixture.debugElement.queryAll(By.css('input[type=text]'));
    buttons = fixture.debugElement.queryAll(By.css('button'));
    appList = [{name: 'firstApp', id: 'firstId'}, {name: 'secondApp', id: 'secondId'}];
    spyOn(appService, 'getList').and.returnValues(of(appList));
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should show application list on init', async(() => {
    fixture.whenStable().then(() => {
      buttons = fixture.debugElement.queryAll(By.css('button'));
      expect(component.applications).toEqual(appList);
      expect(buttons.length).toBe(7);
    })
  }));

  it('should delete app when user click delete button', async(() => {
    spyOn(appService, 'delete').and.returnValues(of(new HttpResponse({status: 204})));

    fixture.whenStable().then((() => {
      buttons = fixture.debugElement.queryAll(By.css('button'));
      buttons[1].triggerEventHandler('click', null);

      expect(appService.delete).toHaveBeenCalled();
      expect(component.applications.length).toEqual(1);
      expect(component.applications.find(element => element.id == appList[0].id)).toBeTruthy();
    }))
  }));

  it('should change name of app when user click change name button', async(() => {
    const newName: String = 'superNewNam';
    spyOn(appService, 'changeName').and.returnValues(of(new HttpResponse()));

    fixture.whenStable().then(
      () => {
        buttons = fixture.debugElement.queryAll(By.css('button'));
        buttons[3].triggerEventHandler('click', null);
        fixture.detectChanges();

        inputEl = fixture.debugElement.queryAll(By.css('input[type=text]'));
        inputEl[1].nativeElement.value = newName;
        inputEl[1].nativeElement.dispatchEvent(new Event('input'));


        submitEl = fixture.debugElement.queryAll(By.css('form'));
        submitEl[1].triggerEventHandler('ngSubmit', null);
        fixture.detectChanges();
        expect(appService.changeName).toHaveBeenCalledWith(appList[0].id,newName);
        expect(component.applications.find(app => app.id == appList[0].id).name).toBe(newName);
      }
    );
  }));

  it('should change secret of app when user click resetSecret button', async(() => {
    const newSecret: string = 'superNewName';
    spyOn(appService, 'resetSecret').and.returnValues(of(newSecret));
    fixture.whenStable().then(() => {
      buttons = fixture.debugElement.queryAll(By.css('button'));
      buttons[2].triggerEventHandler('click', null);
      expect(appService.resetSecret).toHaveBeenCalledWith(appList[0].id);
      expect(component.applications.find(element => element.id == appList[0].id)).toBeTruthy();
    })
  }));


  it('should add application ', async(() => {
    const appWrapper: ApplicationWrapper = new ApplicationWrapper('oiuyt', {name: 'asdfg', id: 'zxcvbn'});
    const name: String = 'yoyo';
    spyOn(appService, 'add').and.returnValues(of(appWrapper));

    fixture.whenStable().then(() => {
      inputEl[0].nativeElement.value = name;
      inputEl[0].nativeElement.dispatchEvent(new Event('input'));
      submitEl[0].triggerEventHandler('ngSubmit', null);

      expect(appService.add).toHaveBeenCalledWith(name);
      expect(component.applications).toContain(appWrapper.application);
    })

  }));
});
