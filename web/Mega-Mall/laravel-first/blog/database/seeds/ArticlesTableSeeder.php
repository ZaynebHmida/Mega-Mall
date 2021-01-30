<?php

use Illuminate\Database\Seeder;
use Illuminate\Database\Eloquent\Model;
use App\Article;
class ArticlesTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('Articles')->insert([

            [
                'Price' => '5000',
                'desc' => 'kkkk',
                'cat' => 'c1'
            ],[

                'Price' => '7000',
                'desc' => 'ddd',
                'cat' => 'c2'
            ],[

                'Price' => '9000',
                'desc' => 'qqqq',
                'cat' => 'c3'
            ],[
                'Price' => '2000',
                'desc' => 'ttttt',
                'cat' => 'c4'
            ],[
                'Price' => '3000',
                'desc' => 'aaaa',
                'cat' => 'c6'
            ]
        ]);



    }
}
