QUnit.test('Comment Class - Constructor (normal use)', function(assert) {
  const result = new Comment('normal comment');
  const expectedPerspectiveString = '{"comment":{"text":"normal comment"},"requestedAttributes":{"TOXICITY":{}},"languages":["en"]}';
  assert.equal(result.text, 'normal comment', '0, false; equal succeeds');
  assert.equal(result.perspectiveString, expectedPerspectiveString, '0, false; equal succeeds');
});

QUnit.test('Comment Class - Constructor (no comment input)', function(assert) {
  assert.throws(
    function() {
      new Comment('');
    },
    NO_COMMENT_ERROR,
    'error message for no comment'
  );
});

QUnit.test('Comment Class - Constructor (input size exceeded)', function(assert) {
  const expected = new Error('Comment is too long. We are currently only able to analyze comments of up to approx. 3000 characters.');
  assert.throws(
    function() {
      new Comment('A'.repeat(3001));
    },
    COMMENT_LENGTH_EXCEEDED_ERROR,
    'error message for comment length exceeded'
  );
});
