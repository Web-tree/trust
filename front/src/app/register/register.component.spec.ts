import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RegisterComponent} from './register.component';
import {AlertService} from "../_services/alert.service";
import {UserService} from "../_services/user.service";
import {FormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";
import {Subject} from "rxjs/internal/Subject";
import {By} from "@angular/platform-browser";
import {DebugElement} from "@angular/core";
import {User} from "../_models/user";



describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let submitEl: DebugElement;
  let usernameEl: DebugElement;
  let passwordEl: DebugElement;
  let userService = jasmine.createSpyObj('UserService', ['create']);
  let userName: String = 'someUsername';
  let password: String = 'somePassword';


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, RouterTestingModule, HttpClientModule],
      declarations: [RegisterComponent],
      providers: [{provide: UserService, useValue: userService}, AlertService, Subject]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    submitEl = fixture.debugElement.query(By.css('form'));
    usernameEl = fixture.debugElement.query(By.css('input[type=text]'));
    passwordEl = fixture.debugElement.query(By.css('input[type=password]'));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('When put inputs it add to the model', () => {
    fixture.whenStable().then(() => {

      expect(component.model.username).toBeNull();
      usernameEl.nativeElement.value = userName;
      usernameEl.nativeElement.dispatchEvent(new Event('input'));
      expect(component.model.username).toBe(userName);

      expect(component.model.password).toBeNull();
      passwordEl.nativeElement.value = password;
      passwordEl.nativeElement.dispatchEvent(new Event('input'));
      expect(component.model.password).toBe(password);

    });
  });

  it('When click register model passes to service', () => {

    let user: User = new User("someUsername", "somePassword");
    component.model = user;

    spyOn(component, "register").and.callThrough();
    submitEl.triggerEventHandler('ngSubmit', null);

    expect(component.register).toHaveBeenCalled();
    expect(userService.create).toHaveBeenCalledWith(user);
  });
});
