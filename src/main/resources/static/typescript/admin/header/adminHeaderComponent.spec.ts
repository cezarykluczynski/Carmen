/// <reference path="../../../typings/main/ambient/jasmine/index.d.ts" />
/// <reference path="../../../typings/jquery/jquery.d.ts" />

import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders} from '@angular/core/testing';
import {TestComponentBuilder} from '@angular/compiler/testing/test_component_builder';
import {ComponentFixture} from '@angular/compiler/testing/test_component_builder';
import {Router, ROUTER_PRIMARY_COMPONENT} from '@angular/router-deprecated';
import {RootRouter} from '@angular/router-deprecated/src/router';
import {APP_BASE_HREF} from '@angular/common/src/location/location_strategy';
import {Location} from '@angular/common/src/location/location';
import {provide} from '@angular/core';
import {SpyLocation} from '@angular/common/testing';
import {RouteRegistry} from '@angular/router-deprecated';
import {AppAdminComponent} from '../appAdminComponent';
import {AdminHeaderComponent} from './adminHeaderComponent';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';
import {HttpClient} from '../util/httpClient';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {MockXHR} from '@angular/compiler/testing';

describe('Component: AdminHeaderComponent', () => {
	let adminHeaderComponent: AdminHeaderComponent;
	let element: any;
	let fixture: ComponentFixture<AdminHeaderComponent>;

	beforeEachProviders(() => {
		return [
			COMPILER_PROVIDERS,
			RouteRegistry,
			provide(XHR, { useClass: MockXHR }),
			provide(Location, {useClass: SpyLocation}),
			provide(APP_BASE_HREF, {useValue: '/'}),
			provide(Router, {useClass: RootRouter}),
			provide(RouteRegistry, {useClass: RouteRegistry}),
			provide(HttpClient, { useClass: HttpClient}),
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
});
