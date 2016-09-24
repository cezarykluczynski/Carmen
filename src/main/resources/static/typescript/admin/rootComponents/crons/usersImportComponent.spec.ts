/// <reference path="../../../../typings/jquery/jquery.d.ts" />

import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders} from '@angular/core/testing';
import {TestComponentBuilder} from '@angular/compiler/testing/test_component_builder';
import {ComponentFixture} from '@angular/compiler/testing/test_component_builder';
import {MockBackend} from '@angular/http/testing';
import {Router} from '@angular/router-deprecated';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend} from '@angular/http';
import {provide} from '@angular/core';
import {RootRouter} from '@angular/router-deprecated/src/router';
import {HttpClient} from '../../util/httpClient';
import {HttpClientTestHelper} from '../../util/httpClientTestHelper.spec';
import {UsersImportComponent} from './usersImportComponent';
import {UsersImportApi} from './usersImportApi';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {MockXHR} from '@angular/compiler/testing';

describe('Component: UsersImportComponent', () => {
	let usersImportComponent: UsersImportComponent;
	let connectionBackend: MockBackend;
	let element: any;
	let fixture: ComponentFixture<UsersImportComponent>;

	let createResponse = (asyncResolve: boolean, _connectionBackend) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: asyncResolve,
			connectionBackend: _connectionBackend,
			unsubscribeAfter: true,
			url: '/api/admin/github/cron/users_import'
		}, {
			'highestGitHubUserId': null,
			'enabled': false,
			'running': false
		});
	};

	beforeEachProviders(() => {
		return [
			HTTP_PROVIDERS,
			COMPILER_PROVIDERS,
			provide(XHR, { useClass: MockXHR }),
			provide(Router, { useClass: RootRouter }),
			provide(XHRBackend, { useClass: MockBackend }),
			provide(HttpClient, { useClass: HttpClient }),
			provide(UsersImportComponent, { useClass: UsersImportComponent }),
			provide(UsersImportApi, { useClass: UsersImportApi }),
			provide(ConnectionBackend, { useClass: MockBackend }),
			provide(Http, { useClass: Http })
		];
	});

	beforeEach(inject([ConnectionBackend], (_connectionBackend) => {
		connectionBackend = _connectionBackend;
		createResponse(false, connectionBackend);
	}));

	beforeEach(injectAsync([TestComponentBuilder], tcb => {
		return tcb
			.createAsync(UsersImportComponent)
			.then((_fixture) => {
				fixture = _fixture;
				usersImportComponent = fixture.componentInstance;
				element = fixture.nativeElement;
				fixture.detectChanges();
			});
	}));

	it('should be defined', () => {
		expect(usersImportComponent).not.toBe(undefined);
	});

	it('sets loading flag', done => {
		expect(usersImportComponent.loading).toBe(false);
		createResponse(true, connectionBackend);
		usersImportComponent.refreshStatus();
		expect(usersImportComponent.loading).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent.loading).toBe(false);
			done();
		});
	});

	it('allows reload', done => {
		let $button = jQuery(element).find('button:contains(\'Refresh\')');
		expect(usersImportComponent.loading).toBe(false);
		expect($button.length).toBe(1);

		createResponse(true, connectionBackend);
		$button[0].click();
		fixture.detectChanges();
		expect(usersImportComponent.loading).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent.loading).toBe(false);
			done();
		});
	});

	it('enables cron', (done) => {
		let $button = jQuery(element).find('button:contains(\'Enable\')');
		expect($button.length).toBe(1);

		spyOn(usersImportComponent['usersImportApi'], 'setStatus').and.callThrough();

		createResponse(true, connectionBackend);
		$button[0].click();
		fixture.detectChanges();

		expect(usersImportComponent.isLoading()).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent['usersImportApi'].setStatus).toHaveBeenCalledWith(true);
			expect(usersImportComponent.isLoading()).toBe(false);
			done();
		});
	});

	it('disables cron', (done) => {
		usersImportComponent['enabled'] = true;
		fixture.detectChanges();

		let $button = jQuery(element).find('button:contains(\'Disable\')');
		expect($button.length).toBe(1);

		spyOn(usersImportComponent['usersImportApi'], 'setStatus').and.callThrough();

		createResponse(true, connectionBackend);
		$button[0].click();
		fixture.detectChanges();
		expect(usersImportComponent.isLoading()).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent['usersImportApi'].setStatus).toHaveBeenCalledWith(false);
			expect(usersImportComponent.isLoading()).toBe(false);
			done();
		});
	});
});
