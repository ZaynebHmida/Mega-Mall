<?php

use Illuminate\Http\Request;
Use App\Article;

Route::get('articles', 'ArticleController@index');
Route::get('articles/{article}', 'ArticleController@show');
Route::post('articles', 'ArticleController@store');
Route::put('articles/{article}', 'ArticleController@update');
Route::delete('articles/{article}', 'ArticleController@delete');

Route::get('boutiques', 'BoutiqueController@index');
Route::get('boutiques/{boutique}', 'BoutiqueController@show');
Route::post('boutiques', 'BoutiqueController@store');
Route::put('boutiques/{boutique}', 'BoutiqueController@update');
Route::delete('boutiques/{boutique}', 'BoutiqueController@delete');

Route::get('categories', 'CategorieController@index');
Route::get('categories/{categorie}', 'CategorieController@show');
Route::post('categories', 'CategorieController@store');
Route::put('categories/{categorie}', 'CategorieController@update');
Route::delete('categories/{categorie}', 'CategorieController@delete');

Route::get('reviews', 'ReviewController@index');
Route::get('reviews/{review}', 'ReviewController@show');
Route::post('reviews', 'ReviewController@store');
Route::put('reviews/{review}', 'ReviewController@update');
Route::delete('reviews/{review}', 'ReviewController@delete');


Route::get('users', 'UserController@index');
Route::get('users/{user}', 'UserController@show');
Route::post('users', 'UserController@store');
Route::put('users/{user}', 'UserController@update');
Route::delete('users/{user}', 'UserController@delete');

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});
