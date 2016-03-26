// Source: http://twofuckingdevelopers.com/2016/01/testing-angular-2-with-karma-and-jasmine/
Error.stackTraceLimit = Infinity;

jasmine.DEFAULT_TIMEOUT_INTERVAL = 1000;

__karma__.loaded = function() {};

System.config({
	map: {
		'base/dist/': "/base/dist/"
	},
	packages: {
		'base/dist/': {
			defaultExtension: 'js',
			format: 'register',
			map: Object
				.keys(window.__karma__.files)
				.filter(onlyAppFiles)
				.reduce(createPathRecords, {})
		}
	}
});

System.import('angular2/src/platform/browser/browser_adapter')
	.then(function(browser_adapter) {
		browser_adapter.BrowserDomAdapter.makeCurrent();
	})
	.then(function() {
		return Promise.all(resolveTestFiles());
	})
	.then(function() {
		__karma__.start();
	}, function(error) {
		__karma__.error(error.stack || error);
	});

function createPathRecords(pathsMapping, appPath) {
	var pathParts = appPath.split('/');
	var moduleName = './' + pathParts.slice(Math.max(pathParts.length - 2, 1)).join('/');
	moduleName = moduleName.replace(/\.js$/, '');
	pathsMapping[moduleName] = appPath + '?' + window.__karma__.files[appPath];
	return pathsMapping;
}

function onlyAppFiles(filePath) {
	return /\/base\/dist\/javascript\/(?!.*\.spec\.js$).*\.js$/.test(filePath);
}

function onlySpecFiles(path) {
	return /\.*.spec\.js$/.test(path);
}

function resolveTestFiles() {
	return Object.keys(window.__karma__.files)
		.filter(onlySpecFiles)
		.map(function(moduleName) {
			return System.import(moduleName);
		});
}
