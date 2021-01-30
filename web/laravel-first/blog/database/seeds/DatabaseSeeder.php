<?php

use Illuminate\Database\Seeder;
use App\Article;
use App\Boutique;
use App\Categorie;
use App\User;
use App\Review;
class DatabaseSeeder extends Seeder
{



    public function run()
    {

        $this->call(CategoriesTableSeeder::class);
        $this->call(ArticlesTableSeeder::class);
        $this->call(BoutiquesTableSeeder::class);
        $this->call(ReviewsTableSeeder::class);
        $this->call(UsersTableSeeder::class);
    }
}
