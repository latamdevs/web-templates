var app = angular.module('web-templates-ui', ['ckeditor', 'ui.router']);

app.run(function() {
  // init();
});


app.config(function($stateProvider, $urlRouterProvider) {
  //
  // For any unmatched url, redirect to /state1
  $urlRouterProvider.otherwise("/new-template");
  //
  // Now set up the states
  $stateProvider
    .state('newTemplateState', {
      url: "/new-template",
      templateUrl: "/views/NewTemplateView.html",
      controller: "NewTemplateController"
    })
    .state('listTemplatesState', {
      url: "/list-templates",
      templateUrl: "views/ListTemplatesView.html",
      controller: "ListTemplatesController"
    });
});
