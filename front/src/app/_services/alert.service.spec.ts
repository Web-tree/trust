import {TestBed, inject} from '@angular/core/testing';

import {AlertService} from './alert.service';
import {Router} from "@angular/router";
import {Subject} from "rxjs/internal/Subject";
import {RouterTestingModule} from "@angular/router/testing";
import {of} from "rxjs/internal/observable/of";

describe('Alert Service', () => {

  let alertService: AlertService;
  let router: Router;
  let subjectSpy: any;
  const msg: string = 'testMessage';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AlertService,
        {provide: Subject, useValue: subjectSpy}
      ],
      imports: [
        RouterTestingModule.withRoutes([]),
      ]
    });
    router = TestBed.get(Router);
    subjectSpy = jasmine.createSpyObj('Subject', {'next': null,'asObservable':of(msg)});
    alertService = new AlertService(router, subjectSpy);
  });

  it('#success should call subject.next method', () => {
    alertService.success(msg);
    expect(subjectSpy.next).toHaveBeenCalled();
    expect(subjectSpy.next.calls.count()).toBe(1)
  });

  it('#error should call subject.next method', () => {
    alertService.error(msg);
    expect(subjectSpy.next).toHaveBeenCalled();
    expect(subjectSpy.next.calls.count()).toBe(1)
  });

  it('#getMessage should return expected message', () => {
    alertService.error(msg);
    alertService.getMessage().subscribe(message => {
      expect(message).toEqual(msg);
    });
  });
});
