import {setBaseTestProviders} from '@angular/core/testing';
import {TEST_BROWSER_STATIC_PLATFORM_PROVIDERS,
	TEST_BROWSER_STATIC_APPLICATION_PROVIDERS} from '@angular/platform-browser/testing';

setBaseTestProviders(TEST_BROWSER_STATIC_PLATFORM_PROVIDERS, TEST_BROWSER_STATIC_APPLICATION_PROVIDERS);

window.__carmenConfig = {
	appBaseUrl: '/'
};
