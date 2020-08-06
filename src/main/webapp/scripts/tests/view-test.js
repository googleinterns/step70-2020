QUnit.test('createHeader', function(assert) {
  createHeader();
  assert.dom('.navbar-brand').hasText(TITLE_TEXT);
});

QUnit.test('createMenu', function(assert) {
  createMenu();
  assert.dom('.nav-item').exists({ count: 3});
});

QUnit.test('iFrame created', function(assert) {
  assert.dom('iframe').exists({ count: 1 });
});
