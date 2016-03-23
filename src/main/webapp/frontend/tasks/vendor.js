module.exports = function(grunt) {
	grunt.config.merge({
		concat: {
			options: {
				process: function (src, path) {
					// console.log(path)
					return path.indexOf('.js') === path.length - 3 ? "(function () {\n" + src + "\n})();\n" : src;
				}
			},
			vendorJs: {
				src: [
					'node_modules/es6-shim/es6-shim.js',
					'node_modules/systemjs/dist/system-polyfills.js',
					'node_modules/angular2/es6/dev/src/testing/shims_for_IE.js',
					'node_modules/angular2/bundles/angular2-polyfills.js',
					'node_modules/systemjs/dist/system.src.js',
					'node_modules/rxjs/bundles/Rx.js',
					'node_modules/angular2/bundles/angular2.dev.js',
					'node_modules/angular2/bundles/router.js'
				],
				dest: 'dist/javascript/vendor.js'
			},
			vendorCss: {
				src: [
					'node_modules/bootstrap/dist/css/bootstrap.css'
				],
				dest: 'dist/css/vendor.css'
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-concat');

	grunt.registerTask('vendor-js', function () {
		grunt.task.run('concat:vendorJs');
	});

	grunt.registerTask('vendor-css', function () {
		grunt.task.run('concat:vendorCss');
	});

	grunt.registerTask('vendor', function () {
		grunt.task.run(['vendor-js', 'vendor-css']);
	});
};
