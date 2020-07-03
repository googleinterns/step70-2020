QUnit.test('Comment Class - getError (normal use)', function(assert) {
  const comment = new Comment('normal comment');
  const result = comment.getError();
  const expected = false;
  assert.equal(result, expected, '0, false; equal succeeds');
});

QUnit.test('Comment Class - getError (no comment input)', function(assert) {
  const comment = new Comment('');
  const result = comment.getError();
  const expected = 'No comment inputted';
  assert.equal(result, expected, 'error message for no comment');
});

QUnit.test('Comment Class - getError (input size exceeded)', function(assert) {
  const comment = new Comment('A'.repeat(3001));
  const result = comment.getError();
  const expected = 'Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.';
  assert.equal(result, expected, 'error message for comment length exceeded');
});

QUnit.test('Comment Class - makePerspectiveRequestString (normal use)', function(assert) {
  const comment = new Comment('normal comment');
  const result = comment.makePerspectiveRequestString();
  const expected = '{"comment":{"text":"normal comment"},"requestedAttributes":{"TOXICITY":{}},"languages":["en"]}';
  assert.equal(result, expected, 'JSON string of comment object');
});
