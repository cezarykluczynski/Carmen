module.exports = function(grunt) {
	grunt.task.loadTasks('tasks');

	grunt.task.registerTask('build', function () {
		grunt.task.run('sass');
		grunt.task.run('vendor-js');
		grunt.task.run('vendor-css');
		grunt.task.run('tslint');
		grunt.task.run('ts');
	});

	grunt.task.registerTask('build-dirty', function () {
		grunt.task.run('sass');
		grunt.task.run('vendor-js');
		grunt.task.run('vendor-css');
		grunt.task.run('ts');
	});

	grunt.registerTask('once', ['build']);
	grunt.registerTask('once-dirty', ['build-dirty']);
	grunt.registerTask('default', ['build', 'watch']);
};
