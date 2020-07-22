QUnit.test('createHeader', function(assert) {
  createHeader();
  assert.dom('h3').hasText(TITLE_TEXT);
});

QUnit.test('createMenu', function(assert) {
  createMenu();
  assert.dom('li').exists({ count: 3});
});
