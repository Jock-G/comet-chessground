var board = require('./board');
var pieces = require('./pieces');
var configure = require('./configure');
var fen = require('./fen');
var util = require('./util');

module.exports = function(cfg) {

  this.board = board.defaults;

  this.reconfigure = configure.bind(null, this.board);

  if (cfg) this.reconfigure(cfg);

  this.toggleOrientation = board.toggleOrientation.bind(this.board);

  this.setPieces = this.board.pieces.set.bind(this.board.pieces);

  this.selectSquare = board.selectSquare.bind(this.board);

  this.apiMove = board.apiMove.bind(this.board);
};
