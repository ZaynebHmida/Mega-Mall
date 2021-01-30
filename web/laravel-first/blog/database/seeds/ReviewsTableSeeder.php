<?php

use Illuminate\Database\Seeder;

class ReviewsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('Reviews')->insert([

            [
                'Comment' => 'lkhmm',
                'Rating' => '5'
            ],[

                'Comment' => 'lkhddmm',
                'Rating' => '0'
            ],[

                'Comment' => 'lkhffmm',
                'Rating' => '1'
            ],[
                'Comment' => 'lkhrrrmm',
                'Rating' => '3'
            ],[
                'Comment' => 'lkhttmm',
                'Rating' => '0'
            ]
        ]);

    }
}
