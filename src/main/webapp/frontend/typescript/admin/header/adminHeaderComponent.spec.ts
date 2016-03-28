/// <reference path="../../../typings/main/ambient/jasmine/index.d.ts" />
/// <reference path="../../../typings/jquery/jquery.d.ts" />

import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders, TestComponentBuilder,
	ComponentFixture, setBaseTestProviders} from 'angular2/testing';
import {Router, APP_BASE_HREF, ROUTER_PRIMARY_COMPONENT, Location} from 'angular2/router';
import {provide} from 'angular2/core';
import {SpyLocation} from 'angular2/src/mock/location_mock';
import {RootRouter} from 'angular2/src/router/router';
import {RouteRegistry} from 'angular2/src/router/route_registry';
import {TEST_BROWSER_PLATFORM_PROVIDERS, TEST_BROWSER_APPLICATION_PROVIDERS } from 'angular2/platform/testing/browser';
import {AppAdminComponent} from '../appAdminComponent';
import {AdminHeaderComponent} from './adminHeaderComponent';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';

setBaseTestProviders(TEST_BROWSER_PLATFORM_PROVIDERS, TEST_BROWSER_APPLICATION_PROVIDERS);

let adminHeaderComponent: AdminHeaderComponent;
let element: any;
let fixture: ComponentFixture;

beforeEachProviders(() => {
	return [
		RouteRegistry,
		provide(Location, {useClass: SpyLocation}),
		provide(APP_BASE_HREF, {useValue: '/'}),
		provide(Router, {useClass: RootRouter}),
		provide(AppAdminComponent, {useClass: AppAdminComponent}),
		provide(AdminHeaderComponent, {useClass: AdminHeaderComponent}),
		provide(AdminHeaderElementsProvider, {useClass: AdminHeaderElementsProvider}),
		provide(ROUTER_PRIMARY_COMPONENT, {useValue: AppAdminComponent})
	];
});

beforeEach(inject([AdminHeaderComponent], ahc => {
}));

beforeEach(injectAsync([TestComponentBuilder], tcb => {
	return tcb
		.createAsync(AdminHeaderComponent)
		.then((_fixture) => {
			fixture = _fixture;
			adminHeaderComponent = fixture.componentInstance;
			element = fixture.nativeElement;
			fixture.detectChanges();
		});
}));

describe('Component: AdminHeaderComponent', () => {
	it('should be defined', () => {
		expect(adminHeaderComponent).not.toBe(undefined);
	});

	it('should have default route set to "Servers"', () => {
		expect(adminHeaderComponent.isActive('Servers')).toBe(true);
	});

	it('should be able to change active route', () => {
		expect(adminHeaderComponent.isActive('Crons')).toBe(false);
	});

	it('should change active when element is clicked', (done) => {
		fixture.debugElement.componentInstance.router.subscribe(() => {
			expect(adminHeaderComponent.isActive('Crons')).toBe(true);
			done();
		});
		jQuery(element).find('a[href="/crons"]')[0].click();
	});
});
